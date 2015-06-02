extern printf
extern scanf

extern strcmp
extern strcat
extern strcpy


extern malloc
extern memcpy
extern free


struc map
.index RESD 1
.value RESD 1
.size:
endstruc



struc vecmap
.x1 RESB map.size
.x2 RESB map.size
.size:
endstruc



section .text
global main

glob_assign:
push ebp
mov ebp, esp
mov esp, ebp
pop ebp
ret


main:
push ebp
mov ebp, esp
sub esp, 16
pusha
call glob_assign
popa
pusha
push 8
call malloc
add esp, 4
mov [ebp - 4], eax
popa
push tmp9
pop eax
push eax
push 256
call malloc
add esp, 4
mov edx, eax
pop eax
push eax
push edx
call strcpy
add esp, 8
mov ecx, [ebp - 4]
mov [ecx +map.value], edx
pusha
push eax
push edx
call strcpy
add esp, 8
popa
push 666
pop eax
mov ecx, [ebp - 4]
mov [ecx + map.index], eax
pusha
push 8
call malloc
add esp, 4
mov [ebp - 8], eax
popa
push dword [ebp - 4]
pop eax
mov ecx, [ebp - 8]
mov edx, [eax  + map.index]
mov [ecx  + map.index], edx
push eax
push 256
call malloc
add esp, 4
mov ebx, eax
pop eax
mov ecx, [ebp - 8]
mov edx, [eax  + map.value]
mov [ecx  + map.value], ebx
pusha
push edx
push ebx
call strcpy
add esp, 8
popa
push tmp10
push str
push 256
call malloc
add esp, 4
pop ebx
pop edx
push edx
push eax
call strcpy
add esp, 8
push ebx
push eax
call strcat
add esp, 8
push eax
pop eax
mov ecx, [ebp - 4]
pusha
push eax
push dword [ecx + map.value]
call strcpy
add esp, 8
popa
mov ecx, [ebp - 4]
push dword [ecx + map.index]
push 667
pop ebx
pop edx
add edx, ebx
mov eax, edx
push eax
pop eax
mov ecx, [ebp - 4]
mov [ecx + map.index], eax
pusha
push 16
call malloc
add esp, 4
mov [ebp - 12], eax
popa
push dword [ebp - 4]
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.index]
mov [ecx + vecmap.x2 + map.index], edx
push eax
push 256
call malloc
add esp, 4
mov ebx, eax
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.value]
mov [ecx + vecmap.x2 + map.value], ebx
pusha
push edx
push ebx
call strcpy
add esp, 8
popa
push dword [ebp - 8]
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.index]
mov [ecx + vecmap.x1 + map.index], edx
push eax
push 256
call malloc
add esp, 4
mov ebx, eax
pop eax
mov ecx, [ebp - 12]
mov edx, [eax  + map.value]
mov [ecx + vecmap.x1 + map.value], ebx
pusha
push edx
push ebx
call strcpy
add esp, 8
popa
push tmp73
push str_format
call printf
add esp, 8
mov ecx, [ebp - 12]
push dword [ecx + vecmap.x1 + map.index]
push int_format
call printf
add esp, 8
push tmp89
push str_format
call printf
add esp, 8
mov ecx, [ebp - 12]
push dword [ecx + vecmap.x1 + map.value]
push str_format
call printf
add esp, 8
push tmp105
push str_format
call printf
add esp, 8
push tmp113
push str_format
call printf
add esp, 8
mov ecx, [ebp - 12]
push dword [ecx + vecmap.x2 + map.index]
push int_format
call printf
add esp, 8
push tmp129
push str_format
call printf
add esp, 8
mov ecx, [ebp - 12]
push dword [ecx + vecmap.x2 + map.value]
push str_format
call printf
add esp, 8
push tmp145
push str_format
call printf
add esp, 8
mov ecx, [ebp - 4]
mov edx, [ecx  + map.value]
pusha
push edx
call free
add esp, 4
popa
pusha
push dword [ebp - 4]
call free
add esp, 4
popa
mov ecx, [ebp - 8]
mov edx, [ecx  + map.value]
pusha
push edx
call free
add esp, 4
popa
pusha
push dword [ebp - 8]
call free
add esp, 4
popa
mov ecx, [ebp - 12]
mov edx, [ecx  + vecmap.x1 + map.value]
pusha
push edx
call free
add esp, 4
popa
mov ecx, [ebp - 12]
mov edx, [ecx  + vecmap.x2 + map.value]
pusha
push edx
call free
add esp, 4
popa
pusha
push dword [ebp - 12]
call free
add esp, 4
popa
mov esp, ebp
pop ebp
ret



section .data
int_format dd "%d", 10, 0
str_format dd "%s", 10, 0
str: dd " chance", 0
tmp1: dd "first", 0
tmp9: dd "first", 0
tmp10: dd "second", 0
tmp11: dd "1. vm.x1.index = ", 0
tmp73: dd "1. vm.x1.index = ", 0
tmp74: dd " vm.x1.value = ", 0
tmp89: dd " vm.x1.value = ", 0
tmp90: dd " ", 0
tmp105: dd " ", 0
tmp106: dd "2. vm.x2.index = ", 0
tmp113: dd "2. vm.x2.index = ", 0
tmp114: dd " vm.x2.value = ", 0
tmp129: dd " vm.x2.value = ", 0
tmp130: dd " ", 0
tmp145: dd " ", 0

section .bss
