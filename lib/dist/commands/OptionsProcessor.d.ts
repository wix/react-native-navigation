import { Store } from '../components/Store';
import { UniqueIdProvider } from '../adapters/UniqueIdProvider';
import { ColorService } from '../adapters/ColorService';
import { AssetService } from '../adapters/AssetResolver';
import { Options } from '../interfaces/Options';
import { Deprecations } from './Deprecations';
export declare class OptionsProcessor {
    private store;
    private uniqueIdProvider;
    private colorService;
    private assetService;
    private deprecations;
    constructor(store: Store, uniqueIdProvider: UniqueIdProvider, colorService: ColorService, assetService: AssetService, deprecations: Deprecations);
    processOptions(options: Options): void;
    processDefaultOptions(options: Options): void;
    private processObject;
    private processColor;
    private processImage;
    private processButtonsPassProps;
    private processComponent;
}
