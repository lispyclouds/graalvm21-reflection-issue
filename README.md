# GraalVM 20/21 native image reflection issue

This is a repro for an issue we are facing as a part of the [Babashka](https://babashka.org) project since GraalVM 21 native image. Also seems to happen with GraalVM 20.

## Build steps
- Download GraalVM or GraalVM CE and set `JAVA_HOME` to the dir.
- Compile: `$JAVA_HOME/bin/javac ReflectionIssue.java`
- Compile to native: `$JAVA_HOME/bin/native-image -H:+UnlockExperimentalVMOptions -H:ReflectionConfigurationFiles=reflect-config.json ReflectionIssue`

## The error

Run the resulting binary `./reflectionissue` to get the following issue (this is on linux/amd64):

```
Fatal error: Cannot invoke method that has a @CallerSensitiveAdapter without an explicit caller

Printing instructions (ip=0x00000000005347be):
  0x00000000005346be: 0x27 0x00 0x48 0x89 0x4c 0x24 0x08 0x48 0x89 0x44 0x24 0x10 0xe8 0x71 0xe7 0xfe
  0x00000000005346ce: 0xff 0x90 0x41 0xc7 0x87 0xe8 0x00 0x00 0x00 0x01 0x00 0x00 0x00 0xf0 0x83 0x04
  0x00000000005346de: 0x24 0x00 0x49 0xc7 0x47 0x08 0x01 0x00 0x00 0x00 0x41 0xc7 0x87 0xe4 0x00 0x00
  0x00000000005346ee: 0x00 0xfe 0xfe 0xfe 0x7e 0x48 0x8b 0x7c 0x24 0x10 0x48 0x8b 0x74 0x24 0x08 0x49
  0x00000000005346fe: 0x8b 0xd6 0xe8 0x8b 0xb5 0xf5 0xff 0x90 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc
  0x000000000053470e: 0xcc 0xcc 0x48 0x83 0xec 0x18 0x48 0x8b 0x44 0x24 0x18 0x49 0x8d 0x8e 0xc8 0x78
  0x000000000053471e: 0x27 0x00 0x48 0x89 0x4c 0x24 0x08 0x48 0x89 0x44 0x24 0x10 0xe8 0x11 0xe7 0xfe
  0x000000000053472e: 0xff 0x90 0x41 0xc7 0x87 0xe8 0x00 0x00 0x00 0x01 0x00 0x00 0x00 0xf0 0x83 0x04
  0x000000000053473e: 0x24 0x00 0x49 0xc7 0x47 0x08 0x01 0x00 0x00 0x00 0x41 0xc7 0x87 0xe4 0x00 0x00
  0x000000000053474e: 0x00 0xfe 0xfe 0xfe 0x7e 0x48 0x8b 0x7c 0x24 0x10 0x48 0x8b 0x74 0x24 0x08 0x49
  0x000000000053475e: 0x8b 0xd6 0xe8 0x2b 0xb5 0xf5 0xff 0x90 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc
  0x000000000053476e: 0xcc 0xcc 0x48 0x83 0xec 0x18 0x48 0x8b 0x44 0x24 0x18 0x48 0x89 0x7c 0x24 0x08
  0x000000000053477e: 0x48 0x89 0x44 0x24 0x10 0xe8 0xb8 0xe6 0xfe 0xff 0x90 0x41 0xc7 0x87 0xe8 0x00
  0x000000000053478e: 0x00 0x00 0x01 0x00 0x00 0x00 0xf0 0x83 0x04 0x24 0x00 0x49 0xc7 0x47 0x08 0x01
  0x000000000053479e: 0x00 0x00 0x00 0x41 0xc7 0x87 0xe4 0x00 0x00 0x00 0xfe 0xfe 0xfe 0x7e 0x48 0x8b
  0x00000000005347ae: 0x7c 0x24 0x10 0x48 0x8b 0x74 0x24 0x08 0x49 0x8b 0xd6 0xe8 0xd2 0xb4 0xf5 0xff
> 0x00000000005347be: 0x90 0xcc 0x48 0x83 0xec 0x18 0x48 0x8b 0x44 0x24 0x18 0x49 0x8d 0x8e 0xe0 0x78
  0x00000000005347ce: 0x27 0x00 0x48 0x89 0x4c 0x24 0x08 0x48 0x89 0x44 0x24 0x10 0xe8 0x61 0xe6 0xfe
  0x00000000005347de: 0xff 0x90 0x41 0xc7 0x87 0xe8 0x00 0x00 0x00 0x01 0x00 0x00 0x00 0xf0 0x83 0x04
  0x00000000005347ee: 0x24 0x00 0x49 0xc7 0x47 0x08 0x01 0x00 0x00 0x00 0x41 0xc7 0x87 0xe4 0x00 0x00
  0x00000000005347fe: 0x00 0xfe 0xfe 0xfe 0x7e 0x48 0x8b 0x7c 0x24 0x10 0x48 0x8b 0x74 0x24 0x08 0x49
  0x000000000053480e: 0x8b 0xd6 0xe8 0x7b 0xb4 0xf5 0xff 0x90 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc
  0x000000000053481e: 0xcc 0xcc 0x48 0x8d 0x5c 0x24 0xf8 0x49 0x3b 0x5f 0x08 0x0f 0x86 0x01 0xb8 0xf3
  0x000000000053482e: 0xff 0x48 0x8b 0xe3 0x49 0x3b 0xf6 0x0f 0x84 0x24 0x00 0x00 0x00 0x8b 0x06 0xc1
  0x000000000053483e: 0xe8 0x05 0x81 0xf8 0x0d 0xa6 0x06 0x00 0x0f 0x85 0x18 0x00 0x00 0x00 0x48 0x8b
  0x000000000053484e: 0xc6 0x48 0x83 0xc4 0x08 0x41 0x83 0x6f 0x10 0x01 0x0f 0x8e 0xa2 0x23 0x00 0x00
  0x000000000053485e: 0xc3 0x49 0x8b 0xf6 0xeb 0xe8 0x49 0x8d 0x86 0x68 0x30 0x35 0x00 0x48 0x8b 0xfe
  0x000000000053486e: 0x48 0x8b 0xf0 0xe8 0xaa 0x60 0xfe 0xff 0x90 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc 0xcc
  0x000000000053487e: 0xcc 0xcc 0x48 0x8d 0x5c 0x24 0x98 0x49 0x3b 0x5f 0x08 0x0f 0x86 0xa1 0xb7 0xf3
  0x000000000053488e: 0xff 0x48 0x8b 0xe3 0x48 0x89 0x7c 0x24 0x38 0x48 0x89 0x74 0x24 0x30 0x48 0x89
  0x000000000053489e: 0x54 0x24 0x28 0x8b 0x46 0x08 0x85 0xc0 0x0f 0x84 0xb9 0x09 0x00 0x00 0x41 0x8b
  0x00000000005348ae: 0x44 0xc6 0x04 0x8b 0x4f 0x04 0x49 0x8d 0x1c 0xce 0x85 0xc0 0x0f 0x84 0xe9 0x05

Top of stack (sp=0x00007ffc797bdfc0):
  0x00007ffc797bdfa0: 0x00007f9b4ca9ec60 0x00000000008758b7 0x0000000100000000 0x00000000005347be
> 0x00007ffc797bdfc0: 0x00007f9b4c4c9190 0x00007f9b4c4f6cc8 0x00000000004ef469 0x00000000004ef469
  0x00007ffc797bdfe0: 0x0000000900000010 0x00000000007023a7 0x00007f9b4c280000 0x00007f9b4c4c9190
  0x00007ffc797be000: 0x00007f9b4c5db950 0x00007f9b4c280000 0x000000094c714180 0x000000000040610d
  0x00007ffc797be020: 0x0000000000000003 0x0000000000a4ed95 0x00007f9b4c760808 0x00007f9b4c4c9190
  0x00007ffc797be040: 0x000000004c6ab5f8 0x000000000040f6e5 0x0000000001234ae0 0x00000000004334ed
  0x00007ffc797be060: 0xffffffffffffffff 0x0000000000000000 0x0000000000000000 0x0000000000000000
  0x00007ffc797be080: 0x0000000001234ae0 0x00007ffc797be1e8 0x0000000100000000 0x00007ffc797be160
  0x00007ffc797be0a0: 0x0000000001232de0 0x00007fa34c538000 0x0000000000000000 0x0000000000000001
  0x00007ffc797be0c0: 0x00007ffc797be1e8 0x00007fa34c31cb8a 0x00007ffc797be110 0x00007ffc797be1e8
  0x00007ffc797be0e0: 0x0000000100400040 0x0000000000433440 0x00007ffc797be1e8 0x223926e85e7556d6
  0x00007ffc797be100: 0x0000000000000001 0x0000000000000000 0x00007fa34c538000 0x0000000001232de0
  0x00007ffc797be120: 0x223926e85d1556d6 0x22874c7c0a3756d6 0x00007fa300000000 0x0000000000000000
  0x00007ffc797be140: 0x0000000000000000 0x0000000000000001 0x00007ffc797be1e0 0xffbf1421f832d000
  0x00007ffc797be160: 0x00007ffc797be1c0 0x00007fa34c31cc4b 0x00007ffc797be1f8 0x0000000001232de0
  0x00007ffc797be180: 0x00007ffc797be1f8 0x0000000000433440 0x0000000000000000 0x0000000000000000
  0x00007ffc797be1a0: 0x0000000000405000 0x00007ffc797be1e0 0x0000000000000000 0x0000000000000000

VM thread locals for the failing thread 0x0000000002f23300:
  0 (8 bytes): JNIThreadLocalEnvironment.jniFunctions = (bytes)
    0x0000000002f23300: 0x00007f9b4c5c1008
  8 (8 bytes): StackOverflowCheckImpl.stackBoundaryTL = (Word) 1 (0x0000000000000001)
  16 (4 bytes): Safepoint.safepointRequested = (int) 2147466737 (0x7fffbdf1)
  20 (4 bytes): StatusSupport.statusTL = (int) 1 (0x00000001)
  24 (32 bytes): ThreadLocalAllocation.regularTLAB = (bytes)
    0x0000000002f23318: 0x00007f9b4ca80000 0x00007f9b4cb00000
    0x0000000002f23328: 0x00007f9b4ca9f9a8 0x0000000000000000
  56 (8 bytes): JavaFrameAnchors.lastAnchor = (Word) 0 (0x0000000000000000)
  64 (8 bytes): PlatformThreads.currentVThreadId = (long) 1 (0x0000000000000001)
  72 (4 bytes): PlatformThreads.currentThread = (Object) java.lang.Thread (0x00007f9b4c710600)
  80 (8 bytes): ActionOnExitSafepointSupport.returnIP = (Word) 0 (0x0000000000000000)
  88 (8 bytes): ActionOnExitSafepointSupport.returnSP = (Word) 0 (0x0000000000000000)
  96 (8 bytes): SubstrateDiagnostics.threadOnlyAttachedForCrashHandler = (bytes)
    0x0000000002f23360: 0x0000000000000000
  104 (8 bytes): ThreadLocalAllocation.allocatedBytes = (Word) 0 (0x0000000000000000)
  112 (8 bytes): VMThreads.IsolateTL = (Word) 140304974348288 (0x00007f9b4c280000)
  120 (8 bytes): VMThreads.OSThreadHandleTL = (Word) 140339334555456 (0x00007fa34c2f2740)
  128 (8 bytes): VMThreads.OSThreadIdTL = (Word) 24990 (0x000000000000619e)
  136 (8 bytes): VMThreads.StackBase = (Word) 140722346651648 (0x00007ffc797bf000)
  144 (8 bytes): VMThreads.StackEnd = (Word) 140722338267136 (0x00007ffc78fc0000)
  152 (8 bytes): VMThreads.StartedByCurrentIsolate = (bytes)
    0x0000000002f23398: 0x0000000000000000
  160 (8 bytes): VMThreads.nextTL = (Word) 0 (0x0000000000000000)
  168 (8 bytes): VMThreads.unalignedIsolateThreadMemoryTL = (Word) 49427168 (0x0000000002f232e0)
  176 (4 bytes): AccessControlContextStack = (Object) java.util.ArrayDeque (0x00007f9b4ca80ed0)
  180 (4 bytes): ExceptionUnwind.currentException = (Object) null
  184 (4 bytes): IsolatedCompileClient.currentClient = (Object) null
  188 (4 bytes): IsolatedCompileContext.currentContext = (Object) null
  192 (4 bytes): JNIObjectHandles.handles = (Object) com.oracle.svm.core.handles.ThreadLocalHandles (0x00007f9b4ca80928)
  196 (4 bytes): JNIThreadLocalPendingException.pendingException = (Object) null
  200 (4 bytes): JNIThreadLocalReferencedObjects.referencedObjectsListHead = (Object) null
  204 (4 bytes): JNIThreadOwnedMonitors.ownedMonitors = (Object) null
  208 (4 bytes): NoAllocationVerifier.openVerifiers = (Object) null
  212 (4 bytes): ThreadingSupportImpl.activeTimer = (Object) null
  216 (4 bytes): ActionOnExitSafepointSupport.actionTL = (int) 0 (0x00000000)
  220 (4 bytes): ActionOnTransitionToJavaSupport.actionTL = (int) 0 (0x00000000)
  224 (4 bytes): ImplicitExceptions.implicitExceptionsAreFatal = (int) 0 (0x00000000)
  228 (4 bytes): StackOverflowCheckImpl.yellowZoneStateTL = (int) 2130640638 (0x7efefefe)
  232 (4 bytes): StatusSupport.safepointBehaviorTL = (int) 1 (0x00000001)
  236 (4 bytes): ThreadingSupportImpl.currentPauseDepth = (int) 0 (0x00000000)

Java frame anchors for the failing thread 0x0000000002f23300:
  No anchors

Stacktrace for the failing thread 0x0000000002f23300 (A=AOT compiled, J=JIT compiled, D=deoptimized, i=inlined):
  i  SP 0x00007ffc797bdfc0 IP 0x00000000005347be size=32    com.oracle.svm.core.jdk.VMErrorSubstitutions.shutdown(VMErrorSubstitutions.java:148)
  i  SP 0x00007ffc797bdfc0 IP 0x00000000005347be size=32    com.oracle.svm.core.jdk.VMErrorSubstitutions.shouldNotReachHere(VMErrorSubstitutions.java:141)
  A  SP 0x00007ffc797bdfc0 IP 0x00000000005347be size=32    com.oracle.svm.core.util.VMError.shouldNotReachHere(VMError.java:90)
  A  SP 0x00007ffc797bdfe0 IP 0x00000000004ef469 size=64    com.oracle.svm.core.reflect.SubstrateMethodAccessor.invoke(SubstrateMethodAccessor.java:112)
  i  SP 0x00007ffc797be020 IP 0x000000000040610d size=48    java.lang.reflect.Method.invoke(Method.java:580)
  A  SP 0x00007ffc797be020 IP 0x000000000040610d size=48    ReflectionIssue.main(ReflectionIssue.java:5)
  i  SP 0x00007ffc797be050 IP 0x000000000040f6e5 size=16    java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
  i  SP 0x00007ffc797be050 IP 0x000000000040f6e5 size=16    com.oracle.svm.core.JavaMainWrapper.invokeMain(JavaMainWrapper.java:181)
  A  SP 0x00007ffc797be050 IP 0x000000000040f6e5 size=16    com.oracle.svm.core.JavaMainWrapper.runCore0(JavaMainWrapper.java:237)
  i  SP 0x00007ffc797be060 IP 0x00000000004334ed size=112   com.oracle.svm.core.JavaMainWrapper.runCore(JavaMainWrapper.java:204)
  i  SP 0x00007ffc797be060 IP 0x00000000004334ed size=112   com.oracle.svm.core.JavaMainWrapper.doRun(JavaMainWrapper.java:293)
  i  SP 0x00007ffc797be060 IP 0x00000000004334ed size=112   com.oracle.svm.core.JavaMainWrapper.run(JavaMainWrapper.java:279)
  A  SP 0x00007ffc797be060 IP 0x00000000004334ed size=112   com.oracle.svm.core.code.IsolateEnterStub.JavaMainWrapper_run_5087f5482cc9a6abc971913ece43acb471d2631b(IsolateEnterStub.java:0)

Threads:
  0x00007f9b44000b80 STATUS_IN_NATIVE (ALLOW_SAFEPOINT) "Reference Handler" - 0x00007f9b4c710588, daemon, stack(0x00007f9b4ba81000,0x00007f9b4c280000)
  0x0000000002f23300 STATUS_IN_JAVA (PREVENT_VM_FROM_REACHING_SAFEPOINT) "main" - 0x00007f9b4c710600, stack(0x00007ffc78fc0000,0x00007ffc797bf000)

No VMOperation in progress

The 30 most recent VM operation status changes:

VM mutexes:
  mutex "RealLog.backTracePrinterMutex" is unlocked.
  mutex "freeList" is unlocked.
  mutex "mainVMOperationControlWorkQueue" is unlocked.
  mutex "referencePendingList" is unlocked.
  mutex "thread" is unlocked.

General information:
  VM version: 21+35, linux/amd64
  Current timestamp: 1695552866769
  VM uptime: 0.008s
  AOT compiled code: 0x0000000000406000 - 0x0000000000afa67f
  CPU features used for AOT compiled code: CX8, CMOV, FXSR, MMX, SSE, SSE2, SSE3, SSSE3, SSE4_1, SSE4_2, POPCNT, LZCNT, AVX, AVX2, BMI1, BMI2, FMA

Command line:

Heap settings and statistics:
  Supports isolates: true
  Heap base: 0x00007f9b4c280000
  Object reference size: 4
  Aligned chunk size: 524288
  Large array threshold: 131072
  Incremental collections: 0
  Complete collections: 0

Heap usage:
  Eden: 0.00M (0.00M in 0 aligned chunks, 0.00M in 0 unaligned chunks)
  Old: 0.00M (0.00M in 0 aligned chunks, 0.00M in 0 unaligned chunks)

Native image heap boundaries:
  ReadOnly Primitives: 0x00007f9b4c300830 - 0x00007f9b4c4af5e0
  ReadOnly References: 0x00007f9b4c4af5e0 - 0x00007f9b4c5c0e28
  ReadOnly Relocatables: 0x00007f9b4c5c1000 - 0x00007f9b4c62d130
  Writable Primitives: 0x00007f9b4c62e000 - 0x00007f9b4c6de548
  Writable References: 0x00007f9b4c6de548 - 0x00007f9b4c817558
  Writable Huge: 0x0000000000000000 - 0x0000000000000000
  ReadOnly Huge: 0x00007f9b4c880038 - 0x00007f9b4ca22c50

Heap chunks: E=eden, S=survivor, O=old, F=free; A=aligned chunk, U=unaligned chunk; T=to space

Fatal error: Cannot invoke method that has a @CallerSensitiveAdapter without an explicit caller
```
