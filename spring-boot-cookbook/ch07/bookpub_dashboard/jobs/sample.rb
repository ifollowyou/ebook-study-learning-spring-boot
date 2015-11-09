require 'httparty'
require 'date'

eden_key = "bookpub.app.memory.pools.PS-Eden-Space.usage"
survivor_key = "bookpub.app.memory.pools.PS-Survivor-Space.usage"
oldgen_key = "bookpub.app.memory.pools.PS-Old-Gen.usage"

SCHEDULER.every '60s' do

  data = JSON.parse(HTTParty.get("http://192.168.59.103:8888/render/?from=-11minutes&target=bookpub.app.memory.pools.PS-Eden-Space.usage&target=bookpub.app.memory.pools.PS-Survivor-Space.usage&target=bookpub.app.memory.pools.PS-Old-Gen.usage&format=json&maxDataPoints=11").body)

  data.each do |metric|
    target = metric["target"]
    # Remove the last data point, which typically has empty value
    data_points = metric["datapoints"][0...-1]
    if target == eden_key
      points = []
      data_points.each_with_index do |entry, idx|
        value = 100 * entry[0] rescue 0
        points << { x: entry[1], y: value.round(0)}
      end
      send_event('heap_eden', points: points)
    elsif target == survivor_key
      current_survivor = 100 * data_points.last[0] rescue 0
      send_event("heap_survivor", { value: current_survivor.round(2)})
    elsif target == oldgen_key
      current_oldgen = 100 * data_points.last[0] rescue 0
      last_oldgen = 100 * data_points[-2][0] rescue 0
      send_event("heap_oldgen", { current: current_oldgen.round(2), last: last_oldgen.round(2)})
    end
  end
end