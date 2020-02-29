import {Directive, ElementRef, HostListener, Input} from '@angular/core';

@Directive({
  selector: '[ehOnlyNumbers]'
})
export class OnlyNumbersDirective {

  @Input() ehOnlyNumbers: boolean;

  constructor(private el: ElementRef) { }

  @HostListener('keydown', ['$event']) onKeyDown(event) {
    const keyboardEvent = <KeyboardEvent> event;
    if (this.ehOnlyNumbers) {
      if ([46, 8, 9, 27, 13, 110, 190].indexOf(keyboardEvent.keyCode) !== -1 ||
          // Allow: Ctrl+A
          (keyboardEvent.keyCode === 65 && (keyboardEvent.ctrlKey || keyboardEvent.metaKey)) ||
          // Allow: Ctrl+C
          (keyboardEvent.keyCode === 67 && (keyboardEvent.ctrlKey || keyboardEvent.metaKey)) ||
          // Allow: Ctrl+V
          (keyboardEvent.keyCode === 86 && (keyboardEvent.ctrlKey || keyboardEvent.metaKey)) ||
          // Allow: Ctrl+X
          (keyboardEvent.keyCode === 88 && (keyboardEvent.ctrlKey || keyboardEvent.metaKey)) ||
          // Allow: home, end, left, right
          (keyboardEvent.keyCode >= 35 && keyboardEvent.keyCode <= 39)) {
        // let it happen, don't do anything
        return;
      }
      // Ensure that it is a number and stop the keypress
      if ((keyboardEvent.shiftKey || (keyboardEvent.keyCode < 48 || keyboardEvent.keyCode > 57)) && (keyboardEvent.keyCode < 96 || keyboardEvent.keyCode > 105)) {
        keyboardEvent.preventDefault();
      }
    }
  }

}
