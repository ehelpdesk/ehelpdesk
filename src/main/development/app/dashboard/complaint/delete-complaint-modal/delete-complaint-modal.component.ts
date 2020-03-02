import { Component, OnInit } from '@angular/core';
import {User} from 'app/model/user.model';
import {UserService} from 'app/model/user.service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {EventManager} from 'app/shared';
import {ComplaintService} from 'app/dashboard/complaint/complaint.service';

@Component({
  selector: 'eh-delete-complaint-modal',
  templateUrl: './delete-complaint-modal.component.html',
  styles: []
})
export class DeleteComplaintModalComponent {

  complaint: User;

  constructor(private complaintService: ComplaintService, public activeModal: NgbActiveModal, private eventManager: EventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(login) {
    this.complaintService.delete(login).subscribe(response => {
      this.eventManager.broadcast({ name: 'complaintListModification', content: 'Deleted a user' });
      this.activeModal.close(true);
    });
  }
}
