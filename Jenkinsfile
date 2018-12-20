#!/usr/bin/env groovy

def scriptVersion  = 'AOS-3004'
def pipelineScript = 'https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git'
fileLoader.withGit(pipelineScript,scriptVersion) {
  jenkinsfile = fileLoader.load('templates/leveransepakke')
}

def config = [
    pipelineScript              : pipelineScript,
    scriptVersion               : scriptVersion,
    javaVersion                 : "8",
    affiliation                 : "paas",
    downstreamSystemtestJob     : [ repo: "openshift-reference-springboot-server", branch: env.BRANCH_NAME],
    debug: true,
    credentialsId: "github",
    suggestVersionAndTagReleases: [
        [branch: 'master', versionHint: '2'],
        [branch: 'release/v1', versionHint: '1']
    ]
]

jenkinsfile.run(scriptVersion, config)
