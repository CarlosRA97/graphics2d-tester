cd ..
./gradlew :g2d-core:createFatJar

#jamvm -cp g2d-core/build/libs/g2d-core.jar org.junit.runner.JUnitCore org.jfree.graphics2d.$1
$1 -cp g2d-core/build/libs/g2d-core.jar org.junit.runner.JUnitCore org.jfree.graphics2d.$2