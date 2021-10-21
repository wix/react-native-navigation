#include <jni.h>
#include <jsi/jsi.h>
#include "jsi_navigation.h"
#include <sys/types.h>

using namespace facebook::jsi;
using namespace std;

JavaVM *java_vm;
jclass java_class;
jobject java_object;

extern "C"
JNIEXPORT void JNICALL
Java_com_reactnativenavigation_react_NavigationModule_nativeInstall(JNIEnv* env,jobject thiz, jlong jsi) {

auto runtime = reinterpret_cast<facebook::jsi::Runtime *>(jsi);
auto navigationJSI = std::make_shared<jsi_navigation::NavigationJSI>();

if (runtime) {
// jsi_navigation::install(*runtime);
jsi_navigation::NavigationJSI::install(*runtime, navigationJSI);

//install(*runtime);
}

env->GetJavaVM(&java_vm);
java_object = env->NewGlobalRef(thiz);
}