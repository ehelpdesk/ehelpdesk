import { NgModule, LOCALE_ID } from '@angular/core';

import { SharedLibsModule, EventManager } from './';
import { AlertComponent } from 'app/shared/alert/alert.component';
import {AlertErrorComponent} from 'app/shared/alert/alert-error.component';

@NgModule({
    imports: [SharedLibsModule],
    declarations: [AlertComponent, AlertErrorComponent],
    providers: [],
    exports: [SharedLibsModule, AlertComponent, AlertErrorComponent]
})
export class SharedCommonModule {
    constructor() {}
}
