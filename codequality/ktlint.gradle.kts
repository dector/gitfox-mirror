val ktlint: Configuration by configurations.creating

dependencies {
    ktlint("com.pinterest:ktlint:0.33.0")
}

tasks.register<JavaExec>("ktlint") {
    group = "verification"
    classpath = ktlint
    main = "com.pinterest.ktlint.Main"
    args(
        "src/**/*.kt",
        "--format",
        "--experimental",
        "--reporter=plain",
        "--reporter=checkstyle,output=${buildDir}/reports/checkstyle/ktlint-report.xml"
    )
}