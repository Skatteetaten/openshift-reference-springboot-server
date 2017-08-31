#!/usr/bin/env groovy

def version = 'v3.1.1'
fileLoader.withGit('https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git', version) {
   jenkinsfile = fileLoader.load('templates/leveransepakke')
}

def systemtest = [
  name : 'systemtest',
  setupCommand: """aoc setup openshift/@TEST_NAME@ \
                   -f about.json '{ "envName" : "-@TEST_NAME@-@TEST_ID@" }' \
                   -f reference.json '{ "build" : { "VERSION" : "@TEST_ID@" }}'""",
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
