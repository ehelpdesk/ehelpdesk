import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';
import { SharedModule } from '../../shared';

@NgModule({
    imports: [SharedModule, RouterModule.forChild([auditsRoute])],
    declarations: [AuditsComponent]
})
export class AuditsModule {}
