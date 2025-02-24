 export type Product = {
  id: string;
  name: string;
  description: string;
  price: number;
  weight: number;
  categoryId: string;
  categoryName: string;
  imageUrl: string;
};

export type CartItem = {
  product: Product;
  quantity: number;
  totalPrice: number;
};