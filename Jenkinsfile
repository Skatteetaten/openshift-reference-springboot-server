#!/usr/bin/env groovy

def version = 'v4.0.0'
fileLoader.withGit('https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git', version) {
   jenkinsfile = fileLoader.load('templates/leveransepakke')
}

def systemtest = [
  auroraConfigEnvironment : 'st-refapp',
  path : 'src/systemtest',
  applicationUnderTest : "referanse",
  npmScripts : ['test']
]

def overrides = [
  affiliation: "paas",
  testStages:[systemtest],
  piTests: false,
  // We had to disable sonar integration because Jenkins terminated the report generation and failed the build
  // completely. There is a Jira task (https://aurora/jira/browse/AOS-1951) to investigate the issue.
  sonarQube: false,
  credentialsId: "github"
  ]

jenkinsfile.run(version, overrides)
