import {Directive, ElementRef, EventEmitter, HostListener, Input, Output} from '@angular/core';

@Directive({
  selector: '[ehOutsideClick]'
})
export class OutsideClickDirective {

  @Output() public outsideClick = new EventEmitter<any>();
  @Input() ignoreIds = '';
  constructor(private elementRef: ElementRef) {}

  @HostListener('document:click', ['$event.target'])
  public onClick(targetElement) {
    const clickedInside = this.elementRef.nativeElement.contains(targetElement);
    const ignorableIds = this.ignoreIds.split(',');
    if (!clickedInside && !ignorableIds.includes(targetElement.getAttribute('id'))
      && targetElement.tagName !== 'path'
      && targetElement.tagName !== 'svg') {
      this.outsideClick.emit(null);
    }
  }

}
