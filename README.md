Gradle dependency

app → feature → domain
app → core, data
feature → domain (NOT data, NOT core directly)
data → domain, core
core → (kisi pe depend nahi karta, sabse neeche)

domain - does not depend on anything

core:network - (retrofit ,APIService, ErrorState) - sees the domain mapper ke liye 
core:di - Hilt Modules dependency injection - will see both domain and core network
data - Repository implementation- sees both domain and core network
feature - userlist - will watch domain
feature - userlist deta
