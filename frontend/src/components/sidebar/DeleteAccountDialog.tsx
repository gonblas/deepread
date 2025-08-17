"use client";

import { useState } from "react";
import { useAuth } from "@/contexts/authContext";
import { useNotification } from "@/contexts/notificationContext";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { AlertTriangle, Trash2 } from "lucide-react";
import Cookies from "js-cookie";

export function DeleteAccountDialog() {
  const { logout } = useAuth();
  const { showSuccess, showError } = useNotification();

  const [deletePassword, setDeletePassword] = useState("");
  const [isDeleting, setIsDeleting] = useState(false);
  const [isOpen, setIsOpen] = useState(false);

  const handleDeleteAccount = async () => {
    if (!deletePassword) {
      showError("Please enter your password to confirm");
      return;
    }

    setIsDeleting(true);
    try {
      const response = await fetch(
        "http://localhost:8080/api/auth",
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
          body: JSON.stringify({
            password: deletePassword,
          }),
        }
      );

      if (!response.ok) {
        if (response.status === 400) {
          showError("Invalid password", "Password must be between 8 and 64 characters")
          return;
        }
        if (response.status === 401) {
          showError("Invalid password", "Please try again with a different password.")
          return;
        }
        throw new Error("Failed to delete account");
      }

      showSuccess("Account deleted successfully");
      logout();
      setIsOpen(false);
    } catch (error) {
      showError("Failed to delete account");
    } finally {
      setIsDeleting(false);
      setDeletePassword("");
    }
  };

  const handleCancel = () => {
    setDeletePassword("");
    setIsOpen(false);
  };

  return (
    <AlertDialog open={isOpen} onOpenChange={setIsOpen}>
      <AlertDialogTrigger asChild>
        <Button
          variant="ghost"
          size="sm"
          className="w-full justify-start text-destructive hover:text-destructive hover:bg-destructive/10"
        >
          <Trash2 className="h-4 w-4 mr-2" />
          Delete Account
        </Button>
      </AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle className="flex items-center gap-2 text-destructive">
            <AlertTriangle className="h-5 w-5" />
            Delete Account
          </AlertDialogTitle>
          <AlertDialogDescription className="space-y-3">
            <>
              <strong>This action cannot be undone.</strong> This will
              permanently delete your account and remove all your data from our
              servers, including:
            </>
            <ul className="list-disc list-inside space-y-1 text-sm my-3">
              <li>All your quizzes and questions</li>
              <li>Quiz attempts and results</li>
              <li>Books and chapters</li>
              <li>Account settings and preferences</li>
            </ul>
            <span className="font-medium text-foreground">
              Please enter your password to confirm:
            </span>
          </AlertDialogDescription>
        </AlertDialogHeader>

        <div className="space-y-2">
          <Label htmlFor="delete-password">Password</Label>
          <Input
            id="delete-password"
            type="password"
            placeholder="Enter your password"
            value={deletePassword}
            onChange={(e) => setDeletePassword(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter" && deletePassword) {
                handleDeleteAccount();
              }
            }}
          />
        </div>

        <AlertDialogFooter>
          <AlertDialogCancel onClick={handleCancel}>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDeleteAccount}
            disabled={!deletePassword || isDeleting}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            {isDeleting ? "Deleting..." : "Delete Account"}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
