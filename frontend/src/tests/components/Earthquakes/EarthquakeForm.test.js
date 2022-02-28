import { render, waitFor, fireEvent } from '@testing-library/react';
import EarthquakeForm from 'main/components/Earthquakes/EarthquakeForm';
import { earthquakesFixtures } from 'fixtures/earthquakesFixtures';
import { BrowserRouter as Router } from 'react-router-dom';

const mockedNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockedNavigate,
}));

describe('EarthquakeForm tests', () => {
  test('renders correctly ', async () => {
    const { getByText } = render(
      <Router>
        <EarthquakeForm />
      </Router>
    );
    // await waitFor(() => expect(getByText(/Quarter YYYYQ/)).toBeInTheDocument());
    await waitFor(() => expect(getByText(/Retrieve/)).toBeInTheDocument());
  });

  // test('renders correctly when passing in an Earthquake', async () => {
  //   const { getByText, getByTestId } = render(
  //     <Router>
  //       <EarthquakeForm
  //         initialEarthquakes={earthquakesFixtures.twoEarthquakes}
  //       />
  //     </Router>
  //   );
  //   await waitFor(() =>
  //     expect(getByTestId('EarthquakeForm-distance')).toBeInTheDocument()
  //   );
  //   expect(getByText(/distance/)).toBeInTheDocument();
  //   expect(getByTestId('EarthquakeForm-distance')).toHaveValue('5');
  // });

  test('Correct Error messages on missing input', async () => {
    const { getByTestId, getByText } = render(
      <Router>
        <EarthquakeForm />
      </Router>
    );
    await waitFor(() =>
      expect(getByTestId('EarthquakeForm-submit')).toBeInTheDocument()
    );
    const submitButton = getByTestId('EarthquakeForm-submit');

    fireEvent.click(submitButton);

    await waitFor(() =>
      expect(getByText(/Distance is required./)).toBeInTheDocument()
    );
    expect(getByText(/Magnitude is required./)).toBeInTheDocument();
  });

  test('No Error messsages on good input', async () => {
    const mockSubmitAction = jest.fn();

    const { getByTestId, queryByText } = render(
      <Router>
        <EarthquakeForm submitAction={mockSubmitAction} />
      </Router>
    );
    await waitFor(() =>
      expect(getByTestId('EarthquakeForm-distance')).toBeInTheDocument()
    );

    const distanceField = getByTestId('EarthquakeForm-distance');

    const minMagField = getByTestId('EarthquakeForm-minMag');

    const submitButton = getByTestId('EarthquakeForm-submit');

    fireEvent.change(distanceField, { target: { value: '5' } });
    fireEvent.change(minMagField, {
      target: { value: '6.9' },
    });
    fireEvent.click(submitButton);

    await waitFor(() => expect(mockSubmitAction).toHaveBeenCalled());

    expect(queryByText(/Distance is required./)).not.toBeInTheDocument();
    expect(queryByText(/Magnitude is required./)).not.toBeInTheDocument();
  });

  test('Test that navigate(-1) is called when Cancel is clicked', async () => {
    const { getByTestId } = render(
      <Router>
        <EarthquakeForm />
      </Router>
    );
    await waitFor(() =>
      expect(getByTestId('EarthquakeForm-cancel')).toBeInTheDocument()
    );
    const cancelButton = getByTestId('EarthquakeForm-cancel');

    fireEvent.click(cancelButton);

    await waitFor(() => expect(mockedNavigate).toHaveBeenCalledWith(-1));
  });
});