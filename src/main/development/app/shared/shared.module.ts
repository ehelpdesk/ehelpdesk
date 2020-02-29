import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {SharedCommonModule, SharedLibsModule, StorageManagerService} from './';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';

import {DatePipe} from '@angular/common';
import {NoActionDirective} from 'app/shared/directive/no-action.directive';
import {LoaderComponent} from 'app/shared/loader/loader.component';
import {OutsideClickDirective} from 'app/shared/directive/outside-click.directive';
import {OnlyNumbersDirective} from 'app/shared/directive/only-numbers.directive';

@NgModule({
  imports: [SharedLibsModule, SharedCommonModule, FontAwesomeModule],
  declarations: [NoActionDirective, LoaderComponent, OutsideClickDirective, OnlyNumbersDirective],
  providers: [DatePipe, StorageManagerService],
  entryComponents: [],
  exports: [SharedCommonModule, DatePipe, FontAwesomeModule, NoActionDirective, LoaderComponent, OutsideClickDirective, OnlyNumbersDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SharedModule {
}
