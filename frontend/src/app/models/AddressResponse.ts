export interface AddressResponse {
	id: number;
	zipCode: number;
	city: string;
	street: string;
	houseNumber: string;
	userId: number;
}

export function addressToString(address: AddressResponse): string {
	return (
		address.zipCode +
		' ' +
		address.city +
		', ' +
		address.street +
		' ' +
		address.houseNumber
	);
}
