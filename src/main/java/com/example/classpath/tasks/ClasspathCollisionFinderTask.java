package com.example.classpath.tasks;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.Resource;
import io.github.classgraph.ResourceList;
import io.github.classgraph.ScanResult;
import java.util.List;
import java.util.Map;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.TaskAction;


public class ClasspathCollisionFinderTask extends DefaultTask {

  private static final Logger LOG = Logging.getLogger(ClasspathCollisionFinderTask.class);

  private FileCollection _classpath;

  @TaskAction
  public void findCollisions() {
    ClassGraph scanner = new ClassGraph()
        .overrideClasspath(_classpath);

    try (ScanResult result = scanner.scan()) {
      List<Map.Entry<String, ResourceList>> duplicatePaths =
          result.getAllResources().classFilesOnly().findDuplicatePaths();

      if (!duplicatePaths.isEmpty()) {
        LOG.error("Duplicate classes on the classpath:");
        for (Map.Entry<String, ResourceList> duplicateEntry : duplicatePaths) {
          LOG.error("{} in [", duplicateEntry.getKey());
          for (Resource resource : duplicateEntry.getValue()) {
            LOG.error("  {}", resource);
          }
          LOG.error("]");
        }
        throw new GradleException("Found duplicate classes. Please see output above.");
      }
    }
  }

  public FileCollection getClasspath() {
    return _classpath;
  }

  public void setClasspath(FileCollection classpath) {
    _classpath = classpath;
  }
}
