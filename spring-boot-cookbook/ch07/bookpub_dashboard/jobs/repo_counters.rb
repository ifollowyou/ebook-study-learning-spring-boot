require 'httparty'

repos = ['AuthorRepository', 'ReviewerRepository', 'BookRepository', 'PublisherRepository']

SCHEDULER.every '10s' do
  data = JSON.parse(HTTParty.get("http://localhost:8080/metrics").body)
  repo_counts = []
  repos.each do |repo|
    current_count = data["counter.datasource.#{repo}"]
    repo_counts << { label: repo, value: current_count }
  end
  send_event('repositories', { items: repo_counts })
end