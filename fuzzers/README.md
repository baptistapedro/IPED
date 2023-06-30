# Fuzzing with Jazzer
"Jazzer is a coverage-guided, in-process fuzzer for the JVM platform developed by Code Intelligence. It is based on libFuzzer and brings many of its instrumentation-powered mutation features to the JVM."

## Vulnerabilities that Jazzer tests
```
INFO: Loaded 166 hooks from com.code_intelligence.jazzer.runtime.TraceCmpHooks
INFO: Loaded 4 hooks from com.code_intelligence.jazzer.runtime.TraceDivHooks
INFO: Loaded 2 hooks from com.code_intelligence.jazzer.runtime.TraceIndirHooks
INFO: Loaded 4 hooks from com.code_intelligence.jazzer.runtime.NativeLibHooks
INFO: Loaded 5 hooks from com.code_intelligence.jazzer.sanitizers.Deserialization
INFO: Loaded 5 hooks from com.code_intelligence.jazzer.sanitizers.ExpressionLanguageInjection
INFO: Loaded 70 hooks from com.code_intelligence.jazzer.sanitizers.LdapInjection
INFO: Loaded 46 hooks from com.code_intelligence.jazzer.sanitizers.NamingContextLookup
INFO: Loaded 1 hooks from com.code_intelligence.jazzer.sanitizers.OsCommandInjection
INFO: Loaded 48 hooks from com.code_intelligence.jazzer.sanitizers.ReflectiveCall
INFO: Loaded 8 hooks from com.code_intelligence.jazzer.sanitizers.RegexInjection
INFO: Loaded 16 hooks from com.code_intelligence.jazzer.sanitizers.RegexRoadblocks
INFO: Loaded 3 hooks from com.code_intelligence.jazzer.sanitizers.ServerSideRequestForgery
INFO: Loaded 19 hooks from com.code_intelligence.jazzer.sanitizers.SqlInjection
INFO: Loaded 6 hooks from com.code_intelligence.jazzer.sanitizers.XPathInjection
```

## Jazzer implementation for IPED
The jazzer standalone artifact can be fetched at _https://github.com/CodeIntelligenceTesting/jazzer/releases_, though the latest version for Linux (v0.19.0) `jazzer_standalone.jar` and `jazzer` binary are already uploaded in this repository, so there is no need to fetch them again.

## How to compile a Fuzzer Harness
1. Add a new class to your project with a `public static void fuzzerTestOneInput(FuzzedDataProvider data)` method.
2. Compile your fuzz test to a class file with jazzer_standalone.jar on the classpath with other artifacts dependencies.
3. With the fuzz test class file compile it to a JAR file.
4. Run the jazzer binary (jazzer.exe on Windows), specifying the classpath (including the harness JAR file) and fuzz test class.

## Working Example
On the directory `./fuzztests/` there is a fuzzer harness example targeting `iped.parsers.mail.MSGParser`. In order to compile the fuzz test harness, the procedure applied was the following:

### Generating the fuzz test harness class file
`
javac -cp "jazzer_standalone.jar:commons-io-2.12.0.jar:poi-5.2.3.jar:tika-core-2.4.0-p1.jar:tika-parsers-standard-package-2.4.0-p1.jar:iped-parsers-impl-4.2-snapshot.jar:log4j-api-2.1.jar" ./fuzztests/ExtractorFuzz.java `


### Generating the fuzz test harness JAR file
`jar cvf ExtractorFuzz.jar ./fuzztests/ExtractorFuzz.class`


### Run fuzzer harness with Jazzer
Do not forget to put the `ExtractorFuzz.jar` in the classpath, then use the `--target_class` flag to set the fuzzer harness class, such as:

`
./jazzer --cp=".:jazzer_standalone.jar:commons-io-2.12.0.jar:poi-5.2.3.jar:tika-core-2.4.0-p1.jar:tika-parsers-standard-package-2.4.0-p1.jar:iped-parsers-impl-4.2-snapshot.jar:log4j-api-2.1.jar:ExtractorFuzz.jar" --target_class=ExtractorFuzz
`

### Adding a Corpus
To add a corpus is just like the libFuzzer syntax, add the path as a position argument, i.e. at the end of the jazzer command line.

### Notes for Continuous Fuzzing
The IPED artifact and other dependencies JARs are available in this repository but you may have to add other dependencies required for the fuzz test harness.
