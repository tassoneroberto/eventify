
import {
  Event
} from '../event';
import {
  Component,
  OnInit,
  Inject
} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogRef
} from '@angular/material';
import {
  FormControl,
  Validators
} from '@angular/forms';
@Component({
  selector: 'mailt-dialog',
  templateUrl: './mailt-dialog.html',
})
export class MessageDialog {

  constructor(public dialogRef: MatDialogRef < MessageDialog > ,
    @Inject(MAT_DIALOG_DATA) public event: Event) {}

  text: string;
  onNoClick(): void {
    this.dialogRef.close();
  }



}
