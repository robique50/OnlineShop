export type OrderProduct = {
  productId: string;
  productName: string;
  quantity: number;
};

export type OrderRequest = {
  customerId: string;
  orderTimestamp: string;
  deliveryCountry: string;
  deliveryCity: string;
  deliveryStreetAddress: string;
  products: OrderProduct[];
};

export type DeliveryDetails = {
  country: string;
  city: string;
  streetAddress: string;
};
