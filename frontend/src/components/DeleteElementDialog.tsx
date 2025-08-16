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

import { Trash2 } from "lucide-react";
import { Button } from "./ui/button";
import { useDataRefresh } from "@/contexts/dataRefreshContext";
import { useAuth } from "@/contexts/authContext";
import Cookies from "js-cookie";

export interface DeleteElementDialogProps {
  deleteURL: string;
  redirectURL: string;
  resourceType: string;
  resourceName: string;
  fullWidth?: boolean;
}

function stringToTitleCase(str: string): string {
  return `${str.charAt(0).toUpperCase()}${str.slice(1)}`
  }

export function DeleteElementDialog({
  deleteURL,
  redirectURL,
  resourceType,
  resourceName,
  fullWidth = false,
}: DeleteElementDialogProps) {
  const { logout } = useAuth();
  const { deleteResource } = useDataRefresh();

  const handleDelete = async () => {
    const responseFunc = async () => {
      const response = await fetch(deleteURL,
        {
          method: "DELETE",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${Cookies.get("token")}`,
          },
        }
      );

      if (!response.ok) {
        if (response.status === 401) {
          logout();
          throw new Error("Authentication failed");
        } else {
          throw new Error(`Failed to delete ${resourceType}: ${response.statusText}`);
        }
      }

      if (response.status === 204) {
        return;
      }

      const text = await response.text();
      return text ? JSON.parse(text) : null;
    };

    try {
      await deleteResource(
        "chapter",
        responseFunc,
        `${stringToTitleCase(resourceType)} "${resourceName}" has been deleted successfully`,
        redirectURL
      );
    } catch (error) {
      console.error("Delete operation failed:", error);
    }
  };

  return (
    <AlertDialog>
      <AlertDialogTrigger asChild>
        <Button
          variant="destructive"
          className={`gap-2 ${fullWidth ? "w-full" : ""}`}
        >
          <Trash2 className="size-4" />
          Delete {resourceType}
        </Button>
      </AlertDialogTrigger>

      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete "
            {resourceName}" and all its associated data.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            Delete {resourceType}
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
}
