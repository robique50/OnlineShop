export type LoginRequest = {
    username: string;
    password: string;
};

export type User= {
    id: string;
    username: string;
    role:'ADMIN'|'CUSTOMER';
};

export interface RegisterRequest {
  username?: string | null | undefined;
  password?: string | null | undefined;
  firstName?: string | null | undefined;
  lastName?: string | null | undefined;
  emailAddress?: string | null | undefined;
  role?: 'CUSTOMER' | 'ADMIN' | null | undefined;
}