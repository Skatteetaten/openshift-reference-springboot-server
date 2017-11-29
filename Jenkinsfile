#!/usr/bin/env groovy

def version = 'feature/AOS-2188-aurora-git-version-v4'
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

def overrides = [
  affiliation: "paas",
  testStages:[systemtest],
  piTests: false,
  credentialsId: "github"
  ]

def props=jenkinsfile.run(version, overrides)

println(props.gitCommitId)