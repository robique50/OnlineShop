export type LoginRequest = {
    username: string;
    password: string;
};

export type User= {
    id: string;
    username: string;
    role:'ADMIN'|'CUSTOMER';
};