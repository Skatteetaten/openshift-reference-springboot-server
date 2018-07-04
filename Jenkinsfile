#!/usr/bin/env groovy

def version = 'v4'
fileLoader.withGit('https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git', version) {
   jenkinsfile = fileLoader.load('templates/leveransepakke')
}

def systemtest = [
  auroraConfigEnvironment : 'st-refapp',
  path : 'src/systemtest',
  applicationUnderTest : "referanse",
  npmScripts : ['test'],
  gatling : [
    "appDir" : "gatling"
  ]
]

def config = [
  affiliation: "paas",
  testStages:[systemtest],
  piTests: false,
  credentialsId: "github"
  ]

jenkinsfile.run(version, config)


