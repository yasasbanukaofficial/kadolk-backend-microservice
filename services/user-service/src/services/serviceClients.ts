import axios, { AxiosError } from 'axios';
import { SiblingNotFoundError, SiblingServiceUnavailableError } from '../errors/custom.errors';

const parkingServiceUrl = process.env.PARKING_SERVICE_URL ?? 'http://localhost:8081';
const vehicleServiceUrl = process.env.VEHICLE_SERVICE_URL ?? 'http://localhost:8082';

const assertResourceExists = async (url: string, notFoundMessage: string): Promise<void> => {
    try {
        await axios.get(url);
    } catch (error) {
        const status = (error as AxiosError).response?.status;
        if (status !== undefined && status >= 400 && status < 500) {
            throw new SiblingNotFoundError(notFoundMessage);
        }
        throw new SiblingServiceUnavailableError('Dependent service is currently unavailable');
    }
};

export const validateParkingExists = async (parkingId: string): Promise<void> =>
    assertResourceExists(`${parkingServiceUrl}/parking/${parkingId}`, `Parking not found with id: ${parkingId}`);

export const validateVehicleExists = async (vehicleId: string): Promise<void> =>
    assertResourceExists(`${vehicleServiceUrl}/vehicle/${vehicleId}`, `Vehicle not found with id: ${vehicleId}`);