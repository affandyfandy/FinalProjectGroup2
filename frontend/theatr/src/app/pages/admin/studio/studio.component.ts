import { CommonModule } from '@angular/common';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { AddStudioDTO, Studio } from '../../../model/studio.model';
import { StudioService } from '../../../services/studio/studio.service';

@Component({
  selector: 'app-studio',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  providers: [
    StudioService
  ],
  templateUrl: './studio.component.html',
  styleUrl: './studio.component.css'
})
export class StudioComponent implements OnInit {

  @ViewChild('studioForm') studioForm: NgForm = {} as NgForm;

  studioList: Studio[] = [];
  currentStudio: Studio = { name: '' };
  currentStudioName = '';
  tempStudioName = '';
  isEdit = false;
  currentPage = 1;
  totalPages = 1;

  isShowAlert = false;
  alertMessage = '';
  isAlertSuccess = true;

  constructor(
    private studioService: StudioService
  ) { }

  ngOnInit(): void {
    this.getStudioList();
  }

  getStudioList(page: number = 0) {
    this.studioService.getStudioList(page).subscribe({
      next: (res: any) => {
        this.studioList = res.content;
        this.currentPage = page;
        this.totalPages = res.totalPages;
      },
      error: (err) => {
        this.showAlert('Failed to get studio list: ' + err.error.message, false);
      }
    });
  }

  addStudio() {
    this.studioService.addStudio({ name: this.currentStudioName }).subscribe({
      next: () => {
        this.getStudioList(this.currentPage);
        this.closeStudioModal();
        this.showAlert('Studio added successfully', true);
      },
      error: (err) => {
        this.closeStudioModal();
        this.showAlert('Failed to add studio: ' + err.error.message, false);
      }
    });
  }

  editStudio() {
    this.studioService.editStudio(this.currentStudio.id!, { name: this.currentStudioName }).subscribe({
      next: () => {
        this.getStudioList(this.currentPage);
        this.closeStudioModal();
        this.showAlert('Studio edited successfully', true);
      },
      error: (err) => {
        this.closeStudioModal();
        this.showAlert('Failed to edit studio: ' + err.error.message, false);
      }
    });
  }

  changeStatus(id: number) {
    this.studioService.changeStatus(id).subscribe({
      next: () => {
        this.showAlert('Studio status changed successfully', true);
        this.getStudioList(this.currentPage);
      },
      error: (err) => {
        this.closeStudioModal();
        this.showAlert('Failed to change studio status: ' + err.error.message, false);
      }
    });
  }

  showStudioModal(isEdit: boolean, studio?: Studio) {
    this.currentStudio = studio || {};
    this.currentStudioName = studio?.name || '';
    this.tempStudioName = studio?.name || '';
    this.isEdit = isEdit;

    const modal = document.getElementById('studio-modal') as HTMLDialogElement;
    modal.showModal();
  }

  closeStudioModal() {
    this.studioForm.resetForm();
    const modal = document.getElementById('studio-modal') as HTMLDialogElement;
    this.tempStudioName = '';
    this.currentStudioName = '';
    modal.close();
  }

  onClickSave() {
    if (this.isEdit) {
      this.editStudio();
    } else {
      this.addStudio();
    }
  }

  showAlert(message: string, success: boolean) {
    this.isAlertSuccess = success;
    this.alertMessage = message;
    this.isShowAlert = true;
    setTimeout(() => {
      this.isShowAlert = false;
    }, 3000);
  }

  isSaveButtonDisabled(): boolean {
    return this.currentStudioName === this.tempStudioName || this.currentStudioName === '' || !this.currentStudioName;
  }
}
