#import "FastOutSlowIn.h"

// ref
// https://github.com/aosp-mirror/platform_frameworks_support/blob/a9ac247af2afd4115c3eb6d16c05bc92737d6305/interpolator/src/main/java/androidx/interpolator/view/animation/FastOutSlowInInterpolator.java
@implementation FastOutSlowIn

CGFloat values[] = {
    0.0000f, 0.0001f, 0.0002f, 0.0005f, 0.0009f, 0.0014f, 0.0020f, 0.0027f, 0.0036f, 0.0046f,
    0.0058f, 0.0071f, 0.0085f, 0.0101f, 0.0118f, 0.0137f, 0.0158f, 0.0180f, 0.0205f, 0.0231f,
    0.0259f, 0.0289f, 0.0321f, 0.0355f, 0.0391f, 0.0430f, 0.0471f, 0.0514f, 0.0560f, 0.0608f,
    0.0660f, 0.0714f, 0.0771f, 0.0830f, 0.0893f, 0.0959f, 0.1029f, 0.1101f, 0.1177f, 0.1257f,
    0.1339f, 0.1426f, 0.1516f, 0.1610f, 0.1707f, 0.1808f, 0.1913f, 0.2021f, 0.2133f, 0.2248f,
    0.2366f, 0.2487f, 0.2611f, 0.2738f, 0.2867f, 0.2998f, 0.3131f, 0.3265f, 0.3400f, 0.3536f,
    0.3673f, 0.3810f, 0.3946f, 0.4082f, 0.4217f, 0.4352f, 0.4485f, 0.4616f, 0.4746f, 0.4874f,
    0.5000f, 0.5124f, 0.5246f, 0.5365f, 0.5482f, 0.5597f, 0.5710f, 0.5820f, 0.5928f, 0.6033f,
    0.6136f, 0.6237f, 0.6335f, 0.6431f, 0.6525f, 0.6616f, 0.6706f, 0.6793f, 0.6878f, 0.6961f,
    0.7043f, 0.7122f, 0.7199f, 0.7275f, 0.7349f, 0.7421f, 0.7491f, 0.7559f, 0.7626f, 0.7692f,
    0.7756f, 0.7818f, 0.7879f, 0.7938f, 0.7996f, 0.8053f, 0.8108f, 0.8162f, 0.8215f, 0.8266f,
    0.8317f, 0.8366f, 0.8414f, 0.8461f, 0.8507f, 0.8551f, 0.8595f, 0.8638f, 0.8679f, 0.8720f,
    0.8760f, 0.8798f, 0.8836f, 0.8873f, 0.8909f, 0.8945f, 0.8979f, 0.9013f, 0.9046f, 0.9078f,
    0.9109f, 0.9139f, 0.9169f, 0.9198f, 0.9227f, 0.9254f, 0.9281f, 0.9307f, 0.9333f, 0.9358f,
    0.9382f, 0.9406f, 0.9429f, 0.9452f, 0.9474f, 0.9495f, 0.9516f, 0.9536f, 0.9556f, 0.9575f,
    0.9594f, 0.9612f, 0.9629f, 0.9646f, 0.9663f, 0.9679f, 0.9695f, 0.9710f, 0.9725f, 0.9739f,
    0.9753f, 0.9766f, 0.9779f, 0.9791f, 0.9803f, 0.9815f, 0.9826f, 0.9837f, 0.9848f, 0.9858f,
    0.9867f, 0.9877f, 0.9885f, 0.9894f, 0.9902f, 0.9910f, 0.9917f, 0.9924f, 0.9931f, 0.9937f,
    0.9944f, 0.9949f, 0.9955f, 0.9960f, 0.9964f, 0.9969f, 0.9973f, 0.9977f, 0.9980f, 0.9984f,
    0.9986f, 0.9989f, 0.9991f, 0.9993f, 0.9995f, 0.9997f, 0.9998f, 0.9999f, 0.9999f, 1.0000f,
    1.0000f};
const int size = (sizeof(values) / sizeof(values[0]) - 1);
const float stepSize = 1.0f / size;

- (CGFloat)interpolate:(CGFloat)progress {
    if (progress >= 1.0f) {
        return 1.0f;
    }
    if (progress <= 0.0f) {
        return 0.0f;
    }

    // Calculate index - We use min with length - 2 to avoid IndexOutOfBoundsException when
    // we lerp (linearly interpolate) in the return statement
    int position = MIN((int)(progress * (size - 1)), size - 2);

    // Calculate values to account for small offsets as the lookup table has discrete values
    float quantized = position * stepSize;
    float diff = progress - quantized;
    float weight = diff / stepSize;

    // Linearly interpolate between the table values
    return values[position] + weight * (values[position + 1] - values[position]);
}

@end