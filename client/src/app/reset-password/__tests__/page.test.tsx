import "@testing-library/jest-dom";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import ResetPassword from "../page";
import { authService } from "@/service/auth/auth";

const pushMock = jest.fn();

jest.mock("next/navigation", () => ({
  useRouter: () => ({ push: pushMock }),
}));

jest.mock("@/service/auth/auth", () => ({
  authService: {
    resetPassword: jest.fn(),
  },
}));

jest.mock("@/contexts/AuthContext", () => ({
  useAuth: () => ({
    logout: jest.fn(),
    isAuthenticated: false,
    isLoading: false,
    user: null,
  }),
}));

describe("ResetPassword", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it("deve exibir toast de sucesso com a mensagem exigida", async () => {
    (authService.resetPassword as jest.Mock).mockResolvedValue(undefined);

    render(<ResetPassword />);

    fireEvent.change(screen.getByPlaceholderText("seu@email.com"), {
      target: { value: "kaue@teste.com" },
    });
    fireEvent.click(screen.getByRole("button", { name: "Enviar Email" }));

    await waitFor(() => {
      expect(
        screen.getByText(/Email enviado com sucesso/)
      ).toBeInTheDocument();
    });
  });
});
