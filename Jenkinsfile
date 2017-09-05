#!/usr/bin/env groovy

def version = 'feature/AOS-1750'
fileLoader.withGit('https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git', version) {
   jenkinsfile = fileLoader.load('templates/leveransepakke')
}

def systemtest = [
  auroraConfigEnvironment : 'st-refapp',
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
