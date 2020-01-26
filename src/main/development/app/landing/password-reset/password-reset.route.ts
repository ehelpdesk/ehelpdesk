import { Route } from '@angular/router';

import {PasswordResetComponent} from 'app/landing/password-reset/password-reset.component';

export const passwordResetRoute: Route = {
    path: 'reset',
    component: PasswordResetComponent,
    data: {
        authority: [],
    }
};
