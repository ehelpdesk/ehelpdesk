import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { User } from 'app/model/user.model';
import { UserService } from 'app/model/user.service';
import { EventManager } from 'app/shared';

@Component({
    selector: 'eh-admin-mgmt-delete-dialog',
    templateUrl: './admin-management-delete-dialog.component.html'
})
export class AdminManagementDeleteDialogComponent {
    user: User;

    constructor(private userService: UserService, public activeModal: NgbActiveModal, private eventManager: EventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(login) {
        this.userService.delete(login).subscribe(response => {
            this.eventManager.broadcast({ name: 'userListModification', content: 'Deleted a user' });
            this.activeModal.close(true);
        });
    }
}
