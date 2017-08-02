package com.demo.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 *
 * Created by panda on 2017/7/28.
 */
class GuardPlugin2 implements Plugin<Project> {
    @Override
    void apply(Project project) {
        System.out.println("========================");
//        AssetGuard.guard(project.getgetAssets(), "mappings.json");
//        project.afterEvaluate{
//            AssetGuard.guard
//        }
        System.out.println("========================");
    }
}