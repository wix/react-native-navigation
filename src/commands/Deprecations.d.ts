export declare class Deprecations {
    private deprecatedOptions;
    checkForDeprecatedOptions(options: Record<string, any>): void;
    onProcessOptions(key: string, parentOptions: Record<string, any>, commandName: string): void;
    onProcessDefaultOptions(_key: string, _parentOptions: Record<string, any>): void;
    private deprecateSearchBarOptions;
    private deprecateInterpolationOptions;
    private deprecateBottomTabsVisibility;
}
