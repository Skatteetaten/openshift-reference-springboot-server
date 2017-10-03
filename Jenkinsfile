#!/usr/bin/env groovy

def version = 'v4.0.0-rc.1'
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
  piTests: false,
  credentials: "github",
  sonarQube: false,
  testStages:[systemtest]
  ]

jenkinsfile.run(version, overrides)
