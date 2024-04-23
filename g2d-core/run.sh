cd ..
./gradlew :g2d-core:createFatJar

jamvm -version
jamvm -Xms64m -Xmx128m -Dsun.java2d.uiScale=1.0 -jar g2d-core/build/libs/g2d-core.jar