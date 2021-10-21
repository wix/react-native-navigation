#include "jsi_navigation.h"


using namespace facebook::jsi;
using namespace std;

namespace jsi_navigation {
void NavigationJSI::install(Runtime &runtime,
                          std::shared_ptr<NavigationJSI> navigationJsi) {
  auto moduleName = "NavigationJSI";
  auto object = facebook::jsi::Object::createFromHostObject(runtime, navigationJsi);
  runtime.global().setProperty(runtime, moduleName, std::move(object));
}
NavigationJSI::NavigationJSI() {}

facebook::jsi::Value NavigationJSI::get(facebook::jsi::Runtime &runtime,
                            const facebook::jsi::PropNameID &name) {
  auto methodName = name.utf8(runtime);

  if (methodName == "setRoot") {
    return facebook::jsi::Function::createFromHostFunction(
        runtime, name, 2,
        [](facebook::jsi::Runtime &runtime, const facebook::jsi::Value &thisValue,
           const facebook::jsi::Value *arguments,
           size_t count) -> facebook::jsi::Value {
              auto componentId= arguments[0].getString(runtime).utf8(runtime);
            return  Value(runtime,
                     String::createFromUtf8(
                                            runtime,
                                            "SetRootFromJSI, componentId:"+componentId));
                 
            });
  }

  if (methodName == "mergeOptions") {
    return facebook::jsi::Function::createFromHostFunction(
        runtime, name, 2,
        [](facebook::jsi::Runtime &runtime, const facebook::jsi::Value &thisValue,
           const facebook::jsi::Value *arguments, size_t count) -> facebook::jsi::Value {
          auto componentId= arguments[0].getString(runtime).utf8(runtime);
            return  Value(runtime,
                     String::createFromUtf8(
                                            runtime,
                                            "MergeOptions JSI, componentId:"+componentId));
        });
  }

  return facebook::jsi::Value::undefined();
}

void install(Runtime &jsiRuntime) {
    auto helloWorld = Function::createFromHostFunction(jsiRuntime,
                                                       PropNameID::forAscii(jsiRuntime,
                                                                            "helloWorld"),
                                                       0,
                                                       [](Runtime &runtime,
                                                          const Value &thisValue,
                                                          const Value *arguments,
                                                          size_t count) -> Value {
        string helloworld = "helloworld";
        
        
        return Value(runtime,
                     String::createFromUtf8(
                                            runtime,
                                            helloworld));
        
    });
    
    jsiRuntime.global().setProperty(jsiRuntime, "helloWorld", move(helloWorld));
    
    auto multiply = Function::createFromHostFunction(jsiRuntime,
                                                     PropNameID::forAscii(jsiRuntime,
                                                                          "multiply"),
                                                     2,
                                                     [](Runtime &runtime,
                                                        const Value &thisValue,
                                                        const Value *arguments,
                                                        size_t count) -> Value {
        int x = arguments[0].getNumber();
        int y = arguments[1].getNumber();
        
        return Value(x * y);
        
    });
    
    jsiRuntime.global().setProperty(jsiRuntime, "multiply", move(multiply));
    
    auto multiplyWithCallback = Function::createFromHostFunction(jsiRuntime,
                                                                 PropNameID::forAscii(jsiRuntime,
                                                                                      "multiplyWithCallback"),
                                                                 3,
                                                                 [](Runtime &runtime,
                                                                    const Value &thisValue,
                                                                    const Value *arguments,
                                                                    size_t count) -> Value {
        int x = arguments[0].getNumber();
        int y = arguments[1].getNumber();
        
        arguments[2].getObject(runtime).getFunction(runtime).call(runtime, x * y);
        
        return Value();
        
    });
    
    jsiRuntime.global().setProperty(jsiRuntime, "multiplyWithCallback", move(multiplyWithCallback));
}

}
