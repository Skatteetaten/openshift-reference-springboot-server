#!/usr/bin/env groovy
def config = [
    scriptVersion          : 'AOS-3004',
    pipelineScript         : 'https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git',
    javaVersion            : "8",
    affiliation            : "paas",
    downstreamSystemtestJob: [repo: "openshift-reference-springboot-server", branch: env.BRANCH_NAME],
    credentialsId          : "github",
    versionStrategy        : [
        [branch: 'master', versionHint: '2'],
        [branch: 'release/v1', versionHint: '1']
    ]
]
fileLoader.withGit(config.pipelineScript, config.scriptVersion) {
  jenkinsfile = fileLoader.load('templates/leveransepakke')
}
jenkinsfile.run(scriptVersion, config)
