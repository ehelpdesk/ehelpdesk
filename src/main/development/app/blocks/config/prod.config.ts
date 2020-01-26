import { enableProdMode } from '@angular/core';
import { DEBUG_INFO_ENABLED } from '../../app.constants';

export function ProdConfig() {
    if (!DEBUG_INFO_ENABLED) {
        enableProdMode();
    }
}
