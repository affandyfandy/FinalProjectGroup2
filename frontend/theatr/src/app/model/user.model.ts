export interface User {
    id?: string;
    email?: string;
    balance?: number;
    role?: string;
    name?: string;
    password?: string;
    confirmPassword?: string;
    createdTime?: Date;
    updatedTime?: Date;
}

export interface LoginDTO {
    email: string;
    password: string;
}

export interface RegisterDTO {
    email: string;
    password: string;
    name: string;
}

export interface TopUpDTO {
    balance: number;
}

export interface ChangePasswordDTO {
    oldPassword: string;
    newPassword: string;
}

export interface UpdateProfileDTO {
    newName: string;
}