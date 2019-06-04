package com.example.classpath;

import com.example.classpath.tasks.ClasspathCollisionFinderTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;


public class ClasspathCollisionFinderPlugin implements Plugin<Project> {
  @Override
  public void apply(Project project) {
    project.getTasks().create("findDuplicateClasses", ClasspathCollisionFinderTask.class);
  }
}
