(use-package emacs
  :config
  (defun rex/mtcg-compile ()
    (interactive)
    (async-shell-command "cd ~/mega/fh/ws22/swen/mtcg/ && make" "*mtcg-compile*"))
 (defun rex/mtcg-run ()
    (interactive)
    (async-shell-command "cd ~/mega/fh/ws22/swen/mtcg/ && make run" "*mtcg-run*"))
 (defun rex/mtcg-curl-script ()
    (interactive)
    (async-shell-command "cd ~/mega/fh/ws22/swen/mtcg/ && make curl-script" "*mtcg-curl-script*"))
 (defun rex/mtcg-kill ()
    (interactive)
    (setq rex/buffers-to-kill '("*mtcg-compile*" "*mtcg-run*" "*mtcg-curl-script*"))
    (dolist (buf rex/buffers-to-kill)
      (kill-matching-buffers buf nil t)))
 :general
 (rex-leader
   :keymaps 'java-mode-map
   "mc" 'rex/mtcg-compile
   "mr" 'rex/mtcg-run
   "ms" 'rex/mtcg-curl-script
   "mk" 'rex/mtcg-kill))
