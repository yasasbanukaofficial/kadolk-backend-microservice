import axios, { AxiosError } from 'axios';
import { SiblingNotFoundError, SiblingServiceUnavailableError } from '../errors/custom.errors';

const parkingServiceUrl = process.env.PARKING_SERVICE_URL ?? 'http://localhost:8003';
const vehicleServiceUrl = process.env.VEHICLE_SERVICE_URL ?? 'http://localhost:8004';

const assertResourceExists = async (url: string, notFoundMessage: string): Promise<void> => {
    try {
        await axios.get(url, { timeout: 5000 });
    } catch (error) {
        const status = (error as AxiosError).response?.status;
        if (status === 404) {
            throw new SiblingNotFoundError(notFoundMessage);
        }
        throw new SiblingServiceUnavailableError('Dependent service is currently unavailable');
    }
};

export const validateParkingExists = async (parkingId: string): Promise<void> =>
    assertResourceExists(`${parkingServiceUrl}/parking/${parkingId}`, `Parking not found with id: ${parkingId}`);

export const validateVehicleExists = async (vehicleId: string): Promise<void> =>
    assertResourceExists(`${vehicleServiceUrl}/vehicle/${vehicleId}`, `Vehicle not found with id: ${vehicleId}`);