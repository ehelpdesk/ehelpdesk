import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'qf-active-login-confirmation',
  templateUrl: './active-login-confirmation.component.html',
  styles: []
})
export class ActiveLoginConfirmationComponent implements OnInit {
  @Output() confirmation: EventEmitter<any> = new EventEmitter();

  constructor(private activeModal: NgbActiveModal) { }

  ngOnInit() {
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirm() {
    this.clear();
    this.confirmation.emit(true);
  }
}
