import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Studio } from '../../../model/studio.model';

@Component({
  selector: 'app-studio',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './studio.component.html',
  styleUrl: './studio.component.css'
})
export class StudioComponent implements OnInit {

  @ViewChild('studioForm') studioForm: NgForm = {} as NgForm;

  studioList: Studio[] = [];
  currentStudioName = '';
  tempStudioName = '';
  isEdit = false;

  constructor() { }

  ngOnInit(): void {
    this.getStudioList();
  }

  getStudioList() {
    // call API to get studio list
    for (let i = 1; i < 10; i++) {
      this.studioList.push({
        id: i,
        name: 'Studio ' + i,
        deleted: false
      });
    }
  }

  showStudioModal(isEdit: boolean, title?: string) {
    this.currentStudioName = title || '';
    this.tempStudioName = title || '';
    this.isEdit = isEdit;

    const modal = document.getElementById('studio-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeStudioModal() {
    this.studioForm.resetForm();
    const modal = document.getElementById('studio-modal') as HTMLDialogElement;
    this.currentStudioName = '';
    this.tempStudioName = '';
    modal.close();
  }
}
