#!/usr/bin/env groovy

node('master') {
    stage("Checkout") {
        checkout scm
    }

    stash 'scm'
}

node('maven') {
    unstash 'scm'

    stage("Build and Deploy") {
        sh "mvn clean package deploy"
    }
}
