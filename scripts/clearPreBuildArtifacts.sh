#!/usr/bin/env bash

function clearPreBuildArtifacts(){
    cd "$ROOT_PROJECT_PATH"
    cd $1
    echo "Current Working Directory "
    pwd
    case $1 in
        rest)
            rm -rf build/
            rm -rf out/
            rm -rf bin/
            ;;
        admin)
            rm -rf build/
            rm -rf out/
            rm -rf bin/
            ;;
        core)
            rm -rf build/
            rm -rf out/
            rm -rf bin/
            ;;
    esac
}

clearPreBuildArtifacts quartz-rest
clearPreBuildArtifacts quartz-admin-thymeleaf
clearPreBuildArtifacts quartz-core