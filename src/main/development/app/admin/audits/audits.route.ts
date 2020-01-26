import { Route } from '@angular/router';

import { AuditsComponent } from './audits.component';
import { ResolvePagingParams } from '../../shared';

export const auditsRoute: Route = {
    path: '',
    component: AuditsComponent,
    resolve: {
        pagingParams: ResolvePagingParams
    },
    data: {
        pageTitle: 'audits.title',
        defaultSort: 'auditEventDate,desc'
    }
};
