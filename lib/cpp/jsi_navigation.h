#ifndef NAVIGATION_H
#define NAVIGATION_H
#include <jsi/jsi.h>
namespace facebook {
namespace jsi {
class Runtime;
class HostObject;
class PropNameID;
class Value;
}
}


namespace jsi_navigation {
class NavigationJSI : public facebook::jsi::HostObject{
    public:
    /*
     * Installs Navigation Object into JavaSctipt runtime.
     */
    static void install(facebook::jsi::Runtime &runtime,
                        std::shared_ptr<NavigationJSI> navigationJSI);

    NavigationJSI();

    /*
     * `jsi::HostObject` specific overloads.
     */
    facebook::jsi::Value get(facebook::jsi::Runtime &runtime, const facebook::jsi::PropNameID &name) override;
};
void install(facebook::jsi::Runtime &jsiRuntime);

}


#endif /* NAVIGATION_H */