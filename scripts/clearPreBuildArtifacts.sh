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
            ;;
        admin)
            rm -rf build/
            rm -rf out/
            ;;
        core)
            rm -rf build/
            rm -rf bin/
            ;;
    esac
}

clearPreBuildArtifacts rest
clearPreBuildArtifacts admin
clearPreBuildArtifacts core