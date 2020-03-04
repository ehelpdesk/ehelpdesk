import {Component, OnInit} from '@angular/core';
import {ComplaintService} from 'app/dashboard/complaint/complaint.service';
import {User} from 'app/model/user.model';
import {AdminManagementDeleteDialogComponent} from 'app/admin/admin-management/admin-management-delete-dialog.component';
import {EventManager} from 'app/shared';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {DeleteComplaintModalComponent} from 'app/dashboard/complaint/delete-complaint-modal/delete-complaint-modal.component';

@Component({
    selector: 'eh-complaints',
    templateUrl: './complaints.component.html',
    styles: []
})
export class ComplaintsComponent implements OnInit {
    complaints: any;

    constructor(private complaintService: ComplaintService
        , private eventManager: EventManager
        , private modalService: NgbModal) {
    }

    ngOnInit() {
        this.loadComplaints();
        this.eventManager.subscribe('complaintListModification', () => {
            this.loadComplaints();
        });
    }

    private loadComplaints() {
        this.complaintService.getComplaints().subscribe(response => {
            this.complaints = response;
        });
    }

    trackIdentity(index, item) {
        return item.id;
    }

    deleteComplaint(complaint: any) {
        const modalRef = this.modalService.open(DeleteComplaintModalComponent, {size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.complaint = complaint;
    }

    openBill(complaint: any) {
        window.open('/bill/' + complaint.id, '_blank');
    }
}
